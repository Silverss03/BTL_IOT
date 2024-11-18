"use client"
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import React, {useState, useEffect} from "react";
import { SessionProvider, useSession } from 'next-auth/react';
import {useSearchParams, useRouter } from "next/navigation";

interface StudentClass {
  section_class_name: string;
  start_time : string, 
  end_time : string,
  check_in_time : string | null
}

interface CalendarEvent {
  title: string;
  start: string;
  end: string;
  description: string;
  color : string;
}

async function getDateMeta(student_id : string | null): Promise<StudentClass[]> {
  const res = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/getStudentClass?studentId=${student_id}`, {
    cache: 'no-store', // Ensures fresh data on each request if needed
  });
  const data = await res.json();
  console.log('Fetched data:', data); // Log the fetched data
  return data;
}

function StudentPage() {
  const searchParams = useSearchParams();
  const userId = searchParams ? searchParams.get("userId") : null;  
  const [events, setEvents] = useState<CalendarEvent[]>([]);
  const { data: session, status } = useSession();
  const router = useRouter();  
  useEffect(() => {
    // if (status === 'loading') return; // Do nothing while loading
    console.log('useEffect called'); // Log when useEffect is called
    // if (!session) {
    //   router.push('/login'); // Redirect to login if not authenticated
    // }
    async function fetchData() {
      const result = await getDateMeta(userId);
      console.log(result);
      const formattedEvents = result.map(event => ({
        title: event.section_class_name,
        start: event.start_time,
        end : event.end_time,
        description : event.check_in_time ? `Điểm danh vào: ${event.check_in_time}` : 'Chưa điểm danh',
        color : event.check_in_time ? 'green' : 'red'
      }));
      setEvents(formattedEvents);
    }
    fetchData();
  }, []);
  return (
    <div className="w-9/12 mt-8">
        <FullCalendar 
            height={"85vh"} 
            plugins={[dayGridPlugin, timeGridPlugin]} 
            headerToolbar={{
                left: "prev,next today", 
                center: "title", 
                right: "timeGridWeek"
            }} 
            initialView="timeGridWeek"
            slotMinTime="06:00:00"   // Set the earliest time slot to 6 AM
            slotMaxTime="20:00:00"   // Set the latest time slot to 8 PM
            events={events}
            eventContent={(eventInfo) => {
                return (
                    <div>
                        <strong>{eventInfo.event.title}</strong>
                        <p>{eventInfo.event.extendedProps.description}</p> {/* Show the description */}
                    </div>
                );
            }}
        />
    </div>
  );
}

export default function Page(){
  return (
    <SessionProvider>
      <StudentPage />
    </SessionProvider>
  );
};