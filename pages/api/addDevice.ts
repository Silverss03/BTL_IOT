import { NextApiRequest, NextApiResponse } from 'next';
import pool from '@/lib/db';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed' });
  }

  const { device_name, device_status } = req.body;

  if (!device_name || !device_status) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  try {
    await pool.query(
      `INSERT INTO device (device_name, device_status) VALUES (?, ?)`,
      [device_name, device_status]
    );

    return res.status(200).json({ message: 'Device added successfully' });
  } catch (error) {
    console.error('Error adding device:', error);
    return res.status(500).json({ error: 'Internal Server Error' });
  }
}