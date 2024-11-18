import { NextApiRequest, NextApiResponse } from "next";
import db from "../../../lib/db"; // Adjust your database connection logic

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  // Extracting the classId from query parameters
  const { classId } = req.query; // Use `req.query` instead of manually parsing the URL

  if (!classId || Array.isArray(classId)) {
    return res.status(400).json({ error: "classId is required and should be a string" });
  }

  try {
    const rows = await db.query(
      `SELECT 
        sc.section_class_name AS section_class_name,
        m.member_name AS student_name,
        s.student_id AS id,
        al.check_in_time AS checkin
      FROM 
        Section_class sc
      JOIN 
        Student_section_class ssc ON sc.section_class_id = ssc.section_class_id
      JOIN 
        attendance_log al ON ssc.student_section_class_id = al.student_section_class_id
      JOIN 
        student s ON ssc.student_id = s.student_id
      JOIN 
        member m ON s.member_id = m.member_id
      WHERE 
        sc.section_class_id = ?`,
      [classId]
    );

    // // If no rows found, return an empty response
    // if (!Array.isArray(rows) || rows.length === 0) {
    //   return res.status(404).json({ error: "Class not found" });
    // }

    // // Mapping the data to the expected response format
    // const classDetail = {
    //   section_class_name: rows[0].section_class_name,
    //   students: rows.map((row: any) => ({
    //     name: row.student_name,
    //     id: row.id,
    //     checkin: row.checkin || "Not checked in", // Default if no check-in time
    //   })),
    // };
    console.log("Fetched class detail:", rows); // Log the fetched data
    return res.status(200).json(rows); // Sending structured response
  } catch (error) {
    console.error("Error fetching class detail:", error);
    res.status(500).json({ error: "Internal Server Error" });
  }
}
