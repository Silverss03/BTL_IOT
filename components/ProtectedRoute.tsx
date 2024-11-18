import { useSession, getSession } from 'next-auth/react';
import { useRouter } from 'next/router';
import { useEffect } from 'react';

import { ReactNode } from 'react';

interface ProtectedRouteProps {
  children: ReactNode;
  role: string;
}

const ProtectedRoute = ({ children, role }: ProtectedRouteProps) => {
  const { data: session, status } = useSession();
  const loading = status === 'loading';
  const router = useRouter();

  useEffect(() => {
    if (!loading) {
      if (!session) {
        router.push('/login');
      } else if (session.user.role !== role) {
        router.push('/unauthorized');
      }
    }
  }, [session, loading, router, role]);

  if (loading || !session) {
    return <div>Loading...</div>;
  }

  return children;
};

export default ProtectedRoute;