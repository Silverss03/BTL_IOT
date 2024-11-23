"use client"
//' -  - ', ' -  - ', ' -  - ', ' -  - '
import { useState, useEffect } from 'react';
import { SessionProvider, useSession, signOut } from 'next-auth/react';
import { useRouter } from 'next/navigation';
import { classNames } from '../lib/utils';
import { useSearchParams } from "next/navigation";

interface Class {
  section_class_name: string;
  section_class_id: string;
  students: Student[];
}

interface Student {
  name: string;
  checkin: string;
}

async function getDateMeta(userId : string | null): Promise<Class[]> {
  const res = await fetch(`http://localhost:3000/api/getTeacherClass?teacherId=${userId}`, {
    cache: 'no-store', // Ensures fresh data on each request if needed
  });
  const data = await res.json();
  return Array.isArray(data) ? data : data.classes || [];;
}

function ClassPage() {
  const searchParams = useSearchParams();
  const userId = searchParams ? searchParams.get("userId") : null;
  const [data, setData] = useState<Class[]>([]);
  const { data: session, status } = useSession();
  const router = useRouter();   

  useEffect(() => {
    if (status === 'loading') return; // Do nothing while loading
    if (!session) {
      router.push('/login'); // Redirect to login if not authenticated
    }
    async function fetchData() {
      const result = await getDateMeta(userId);
      setData(result);
    }
    fetchData();
  }, [session, status, router]);

  const handleClassClick = (id: string) => {
      router.push(`class/${id}`);
  };

  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  const handleLogout = () => {
    signOut({ callbackUrl: '/login' }); // Redirect to login page after logout
  };

  return (
      <div className="container mx-auto px-4">
        <h1 className="text-3xl font-bold mb-8">Classes</h1>
        <button
          onClick={handleLogout}
          className="px-4 py-2 bg-red-500 text-white rounded-md"
        >
          Logout
        </button>
        <ul>
          {data.map((cls) => (
            <li
              key={cls.section_class_id}
              className={classNames(
                'border-b border-gray-200 py-4 px-2 bg-gray-100',
              )}
              onClick={() => handleClassClick(cls.section_class_id)}
            >
              <h2 className="text-lg font-semibold">{cls.section_class_name}</h2>
            </li>
          ))}
        </ul>
      </div>
  );
}

export default function Page() {
  return (
    <SessionProvider>
      <ClassPage />
    </SessionProvider>
  );
}