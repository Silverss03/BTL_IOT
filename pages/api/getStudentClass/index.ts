import { NextApiRequest, NextApiResponse } from 'next';
import pool from '@/lib/db';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  const { studentId } = req.query; // Ensure the parameter name matches the URL
  console.log('studentId:', studentId);

  try {
    // Execute the query
    const [rows] = await pool.query(`
      SELECT 
          sc.section_class_name,
          sc.start_time,
          sc.end_time,
          al.check_in_time
      FROM 
          section_class sc
      JOIN 
          student_section_class ssc ON sc.section_class_id = ssc.section_class_id
      LEFT JOIN 
          attendance_log al ON ssc.student_section_class_id = al.student_section_class_id
      WHERE 
          ssc.student_id = ?;`, [studentId]
      );

    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching data:', error);
    res.status(500).json({ error: 'Error fetching data' });
  }
}