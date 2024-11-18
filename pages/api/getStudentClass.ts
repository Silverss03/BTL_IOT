import { NextApiRequest, NextApiResponse } from 'next';
import pool from '@/lib/db';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  try {

    // Execute the query
    const [rows] = await pool.query(`      
      SELECT 
        sectionclass.name AS class_name,
        attendancelog.check_in_time,
        studentsectionclass.student_id,
        sectionclass.begin AS class_begin,
        sectionclass.end AS class_end
      FROM 
        attendancelog
      JOIN 
        studentsectionclass ON attendancelog.studentsectionclass_id = studentsectionclass.studentsectionclass_id
      JOIN 
        sectionclass ON studentsectionclass.class_id = sectionclass.class_id;`);
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching data:', error);
    res.status(500).json({ error: 'Error fetching data' });
  }
}
