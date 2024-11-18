"use client"
import { useState } from 'react';
import { signIn, getSession } from 'next-auth/react';
import { useRouter } from 'next/navigation';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const router = useRouter();

  const handleSubmit = async (e : any) => {
    e.preventDefault();
    const result = await signIn('credentials', {
      redirect: false,
      username,
      password,
    });

    if (result?.error) {
      console.error(result.error);
    } else {
      // Fetch the session to get the user role
      const session = await getSession();
      if(session){
        const userId = session.user.id;
        console.log('userId:', userId ); // Log the session to check if it contains the user role
        if (session?.user?.role === 'ROLE_STUDENT') {
          router.push(`/dashboard?userId=${userId}`);
        } else if (session?.user?.role === 'ROLE_TEACHER') {
          router.push(`/class?userId=${userId}`);
        } else if (session?.user?.role === 'ROLE_ADMIN') {
          router.push(`/device?userId=${userId}`);
        } else {
          console.error('Unknown role:', session?.user?.role);
        }
      }
    }
  };

  return (
    <div>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Username:
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </label>
        <label>
          Password:
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </label>
        <button type="submit">Login</button>
      </form>
    </div>
  );
}