"use client"
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import React, {useState, useEffect} from "react";
import ProtectedRoute from 'components/ProtectedRoute';
// Example events array
// const events = [
//     {
//       title: "IoT và ứng dụng",
//       start: "2024-11-09T13:00:00",
//       end: "2024-11-09T16:00:00",
//       description: "Chưa điểm danh",
//     },
//     {
//       title: "Lập trình mạng",
//       start: "2024-11-09T07:00:00",
//       end: "2024-11-09T10:00:00",
//       description : "Đã điểm danh: 6:55:00"
//     },
//     {
//       title: "Phân tích thiết kế hệ thống",
//       start: "2024-11-09T10:00:00",
//       end: "2024-11-09T12:00:00",
//       description : 'Đã điểm danh: 12:32:00'
//     }
//   ];

async function getDateMeta(): Promise<any[]> {
  const res = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/getStudentClass`, {
    cache: 'no-store', // Ensures fresh data on each request if needed
  });
  const data = await res.json();
  console.log('Fetched data:', data); // Log the fetched data
  return data;
}

  export default function Page() {
    const [events, setEvents] = useState<any[]>([]);

    useEffect(() => {
      async function fetchData() {
        const result = await getDateMeta();
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
      <ProtectedRoute role="student">
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
      </ProtectedRoute>
    );
}
