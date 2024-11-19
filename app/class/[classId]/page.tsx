"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Modal from '../../../components/Modal';

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
    cache: "no-store",
  });
  if (!res.ok) return null;
  const data = await res.json();
  console.log("Fetched data:", data);
  const classData = data[0];
  if (!classData || classData.length === 0) return null;
  return {
    section_class_name: classData[0].section_class_name,
    students: classData.map((student: any) => ({
      name: student.student_name,
      id: student.id,
      checkin: student.checkin,
    })),
  };
}

async function saveStudentCheckin(student: { name: string; id: string; checkin: string }) {
  const res = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/saveCheckin`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(student),
  });
  if (!res.ok) {
    throw new Error('Failed to save check-in');
  }
}

export default function ClassDetailPage({ params }: { params: { classId: string } }) {
  const router = useRouter();
  const [classId, setClassId] = useState<string | null>(null);
  const [classDetail, setClassDetail] = useState<ClassDetail | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function resolveParams() {
      const resolvedParams = await params;
      setClassId(resolvedParams.classId);
    }

    resolveParams();
  }, [params]);

  useEffect(() => {
    if (!classId) return;

    async function fetchData() {
      setLoading(true);
      const data = await getClassDetail(classId);
      setClassDetail(data);
      setLoading(false);
    }

    fetchData();
  }, [router, classId]);
  const handleOpenModal = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  const handleSubmit = async (student: { name: string; id: string; checkin: string }) => {
    try {
      await saveStudentCheckin(student);
      // Optionally, you can refresh the class details after saving the check-in
      const result = await getClassDetail(classId);
      setClassDetail(result);
    } catch (error) {
      console.error('Error saving check-in:', error);
    }
  };

  if (loading) {
    return <div>Loading class details...</div>;
  }

  if (!classDetail) {
    return <div>Chưa có học sinh nào điểm danh</div>;
  }

  return (
    <div className="container mx-auto px-4">
      <h1 className="text-3xl font-bold mb-4">{classDetail?.section_class_name || 'Class Name'}</h1>
      <button onClick={handleOpenModal} className="mb-4 px-4 py-2 bg-blue-500 text-white rounded-md">
        Điểm danh
      </button>
      <Modal isOpen={isModalOpen} onClose={handleCloseModal} onSubmit={handleSubmit} classId = {classId!}/>
      {classDetail && classDetail.students.length > 0 ? (
        <ul>
          {classDetail.students.map((student) => (
            <li key={student.id} className="border-b border-gray-200 py-2">
              <p className="text-lg font-medium">{student.name}</p>
              <p className="text-sm text-gray-500">Check-in: {student.checkin}</p>
            </li>
          ))}
        </ul>
      ) : (
        <div>Chưa có học sinh nào điểm danh</div>
      )}
    </div>
  );
}
