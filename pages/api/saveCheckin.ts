import { NextApiRequest, NextApiResponse } from 'next';
import pool from '@/lib/db';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed' });
  }

  const { name, id, checkin, classId } = req.body;
  console.log('Request body:', req.body);
  console.log('class:', classId);
  if (!id || !checkin || !classId) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  try {
    // Start a transaction
    await pool.query('START TRANSACTION');
    // Insert into studentsectionclass table
    const result: any = await pool.query(
        `SELECT student_section_class_id FROM student_section_class WHERE section_class_id = ? AND student_id = ?;`,
        [Number(classId), id]
      );
      
      // Ensure you extract the `student_section_class_id` from the result
      
      // Extract the ID from the result
      const studentSectionClassId = result[0]?.student_section_class_id;
      console.log("Fetched student_section_class_id:", studentSectionClassId);
      
      // Use the extracted ID in the second query
      await pool.query(
        `INSERT INTO attendance_log (student_section_class_id, check_in_time) VALUES (?, ?);`,
        [studentSectionClassId, checkin]
      );

    // Commit the transaction
    await pool.query('COMMIT');

    return res.status(200).json({ message: 'Check-in saved successfully' });
  } catch (error) {
    // Rollback the transaction in case of error
    await pool.query('ROLLBACK');
    console.error('Error saving check-in:', error);
    return res.status(500).json({ error: 'Internal Server Error' });
  }
}