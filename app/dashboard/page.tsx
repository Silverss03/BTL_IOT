"use client"
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import React, {useState, useEffect} from "react";
import { SessionProvider, useSession } from 'next-auth/react';
import {useSearchParams, useRouter } from "next/navigation";

interface StudentClass {
  class_name: string;
  class_begin : string, 
  class_end : string,
  check_in_time : string
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
  const [events, setEvents] = useState<any[]>([]);
  const { data: session, status } = useSession();
  const router = useRouter();  
  useEffect(() => {
    if (status === 'loading') return; // Do nothing while loading
    if (!session) {
      router.push('/login'); // Redirect to login if not authenticated
    }
    async function fetchData() {
      const result = await getDateMeta(userId);
      const formattedEvents = result.map(event => ({
        title: event.class_name,
        start: event.class_begin,
        end : event.class_end,
        description: `Checked in at: ${event.check_in_time}`
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