import NextAuth, { User, DefaultSession } from 'next-auth';
import CredentialsProvider from 'next-auth/providers/credentials';
import pool from '@/lib/db';

declare module 'next-auth' {
  interface User {
    id : number;
    role: string;
  }
  interface Session {
    user: {
      id : number;
      role: string;
    } & DefaultSession["user"];
  }
}

async function verifyCredentials(username: string, password: string) {
  const [rows]: any = await pool.query(
    `SELECT 
      member.member_id AS id, 
      member.member_name AS name, 
      member.username, 
      member.password, 
      role.role_name AS role 
    FROM member 
    JOIN role ON member.role_id = role.role_id 
    WHERE member.username = ? AND member.password = ?`,
    [username, password]
  );

  if (rows.length > 0) {
    const user = rows[0];
    return {
      id: user.id,
      name: user.name,
      role: user.role,
    };
  } else {
    return null;
  }
}

export default NextAuth({
  providers: [
    CredentialsProvider({
      name: 'Credentials',
      credentials: {
        username: { label: 'Username', type: 'text' },
        password: { label: 'Password', type: 'password' },
      },
      authorize: async (credentials) => {
        if (!credentials) {
          return Promise.resolve(null);
        }
        const user = await verifyCredentials(credentials.username, credentials.password);
        if (user) {
          return Promise.resolve(user);
        } else {
          return Promise.resolve(null);
        }
      },
    }),
  ],
  callbacks: {
    async jwt({ token, user }) {
      if (user) {
        token.role = user.role;
        token.id = user.id;
      }
      return token;
    },
    async session({ session, token }) {
      if (session.user) {
        session.user.role = token.role as string;
        session.user.id = token.id as number;
      }
      return session;
    },
  },
});