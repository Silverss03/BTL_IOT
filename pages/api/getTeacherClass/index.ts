import { NextApiRequest, NextApiResponse } from 'next';
import pool from '@/lib/db';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  const { teacherId } = req.query; // Ensure the parameter name matches the URL

  try {
    // Execute the query
    const [rows] = await pool.query(`
        SELECT 
          sc.section_class_name,
          sc.section_class_id
        FROM 
          section_class sc
        JOIN 
          teacher_section_class tsc ON sc.section_class_id = tsc.section_class_id
        JOIN
          teacher t ON t.teacher_id = tsc.teacher_id
        JOIN
          member m ON m.member_id = t.member_id
      WHERE 
          m.member_id = ?;`, [teacherId]
      );

    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching data:', error);
    res.status(500).json({ error: 'Error fetching data' });
  }
}