"use client";

import { useState, useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";

interface Student {
  name: string;
  id: string;
  checkin: string;
}

interface ClassDetail {
  section_class_name: string;
  students: Student[];
}

async function getClassDetail(classId: string | null): Promise<ClassDetail | null> {
    const res = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/getClassDetail?classId=${classId}`, {
        cache: "no-store", // Fetch fresh data
    });
  if (!res.ok) return null;
  const data = await res.json();
  console.log("Fetched data:", data); // Log the fetched data
  // Extract the actual class details from the response
  const classData = data[0]; // Assuming the first array contains the actual data

  // If the class data is empty, return null
  if (!classData || classData.length === 0) return null;

  // Format the data to match the structure expected by the frontend
  return {
    section_class_name: classData[0].section_class_name,
    students: classData.map((student: any) => ({
      name: student.student_name,
      id: student.id,
      checkin: student.checkin,
    })),
  };
}

export default function ClassDetailPage({ params }: { params: { classId: string } }) {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { classId } = params;
  const [classDetail, setClassDetail] = useState<ClassDetail | null>(null);

  useEffect(() => {
    if (!classId) {
      router.push("/class"); // Redirect if no classId
      return;
    }

    async function fetchData() {
      const data = await getClassDetail(classId);
      setClassDetail(data);
    }

    fetchData();
  }, [classId, router]);

  if (!classDetail) {
    return <div>Loading...</div>;
  }

  return (
    <div className="container mx-auto px-4">
      <h1 className="text-3xl font-bold mb-4">{classDetail.section_class_name}</h1>
      <ul>
        {classDetail.students.map((student) => (
          <li key={student.id} className="border-b border-gray-200 py-2">
            <p className="text-lg font-medium">{student.name}</p>
            <p className="text-sm text-gray-500">ID: {student.id}</p>
            <p className="text-sm text-gray-500">Check-in: {student.checkin}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}
