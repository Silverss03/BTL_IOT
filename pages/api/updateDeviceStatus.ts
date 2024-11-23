import { NextApiRequest, NextApiResponse } from 'next';
import pool from '@/lib/db';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed' });
  }

  const { id, device_status } = req.body;

  if (!id || !device_status) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  try {
    await pool.query(
      `UPDATE device SET device_status = ? WHERE device_id = ?`,
      [device_status, id]
    );

    return res.status(200).json({ message: 'Device status updated successfully' });
  } catch (error) {
    console.error('Error updating device status:', error);
    return res.status(500).json({ error: 'Internal Server Error' });
  }
}