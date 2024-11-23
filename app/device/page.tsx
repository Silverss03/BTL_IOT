"use client"
import React, {useState, useEffect} from 'react';
import { SessionProvider, useSession } from 'next-auth/react';
import Switch from 'react-switch';

interface Item{
    device_id : number;
    device_name: string;
    device_status: string;
};
    
function AdminPage(){
    const [items, setItems] = useState<Item[]>([]);
    const [newDeviceName, setNewDeviceName] = useState('');
    const [newDeviceStatus, setNewDeviceStatus] = useState('0');
    const [editDeviceId, setEditDeviceId] = useState<number | null>(null);
    const [editDeviceName, setEditDeviceName] = useState('');
    const [editDeviceStatus, setEditDeviceStatus] = useState('0');
    const { data: session, status } = useSession();

    async function getDevice(): Promise<Item[]> {
        const res = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/getDevice`, {
            cache: 'no-store', // Ensures fresh data on each request if needed
        });
        const data = await res.json();
        return Array.isArray(data) ? data : data.classes || [];;
    }

    const handleToggle = async (index: number) => {
        const updatedItems = [...items];
        const currentItem = updatedItems[index];    
        // Toggle the status locally
        currentItem.device_status = currentItem.device_status === '1' ? '0' : '1';
        setItems(updatedItems);
    try {
      // Send a request to update the device status in the backend
        const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/updateDeviceStatus`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: currentItem.device_id,
            device_status: updatedItems[index].device_status,
        }),
        });
        if (!response.ok) {
            throw new Error('Failed to update device status');
        }
        console.log('Device status updated successfully');
        } catch (error) {
            console.error('Error updating device status:', error);
        }
    };
    const handleAddDevice = async () => {
        try {
          const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/addDevice`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              device_name: newDeviceName,
              device_status: newDeviceStatus,
            }),
          });
    
          if (!response.ok) {
            throw new Error('Failed to add device');
          }
    
          console.log('Device added successfully');
          setNewDeviceName('');
          setNewDeviceStatus('0');
          const result = await getDevice();
          setItems(result);
        } catch (error) {
            console.error('Error adding device:', error);
        }
    };

    const handleEditDevice = async () => {
        try {
          const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/editDevice`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              id: editDeviceId,
              device_name: editDeviceName,
              device_status: editDeviceStatus,
            }),
          });
    
          if (!response.ok) {
            throw new Error('Failed to edit device');
          }
    
          console.log('Device edited successfully');
          setEditDeviceId(null);
          setEditDeviceName('');
          setEditDeviceStatus('0');
          const result = await getDevice();
          setItems(result);
        } catch (error) {
            console.error('Error editing device:', error);
        }
    };
    useEffect(() => {
        if (status === 'loading') return; // Do nothing while loading
        async function fetchData() {
            const result = await getDevice();
            setItems(result);
        }
        fetchData();
    }, [session, status]);

    const handleDeleteDevice = async (id: number) => {
        try {
          const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/deleteDevice`, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              id,
            }),
          });
    
          if (!response.ok) {
            throw new Error('Failed to delete device');
          }
    
          console.log('Device deleted successfully');
          const result = await getDevice();
          setItems(result);
        } catch (error) {
            console.error('Error deleting device:', error);
        }
    };

    return (
        <div className="container mx-auto px-4">
            <h1 className="text-3xl font-bold mb-8">Admin Page</h1>
            <div className="mb-8">
                <h2 className="text-2xl font-bold mb-4">Add Device</h2>
                <input
                type="text"
                value={newDeviceName}
                onChange={(e) => setNewDeviceName(e.target.value)}
                placeholder="Device Name"
                className="border border-gray-300 rounded-md p-2 mb-4"
                />
        <select
                value={newDeviceStatus}
                onChange={(e) => setNewDeviceStatus(e.target.value)}
          className="border border-gray-300 rounded-md p-2 mb-4 w-1/6"
        >
          <option value="0">Off</option>
          <option value="1">On</option>
        </select>
        <button
          onClick={handleAddDevice}
          className="px-4 py-2 bg-blue-500 text-white rounded-md"
        >
          Add Device
        </button>
      </div>
      <div className="grid grid-cols-1 gap-4">
        {items.map((item, index) => (
          <div
            key={index}
            className="border border-gray-300 rounded-lg p-4 shadow-md bg-white"
          >
            <h3 className="text-lg font-bold mb-2">{item.device_name}</h3>
            <p className="text-gray-700">Trạng thái: {item.device_status == '1' ? 'Bật' : 'Tắt'}</p>
            <Switch
              onChange={() => handleToggle(index)}
              checked={item.device_status == '1'}
              offColor="#888"
              onColor="#0f0"
            />
            <button
              onClick={() => {
                setEditDeviceId(item.device_id);
                setEditDeviceName(item.device_name);
                setEditDeviceStatus(item.device_status);
              }}
              className="mt-4 px-4 py-2 bg-yellow-500 text-white rounded-md"
            >
              Edit
            </button>
            <button
              onClick={() => handleDeleteDevice(item.device_id)}
              className="mt-4 px-4 py-2 bg-red-500 text-white rounded-md"
            >
              Delete
            </button>
          </div>
        ))}
      </div>
      {editDeviceId && (
        <div className="mt-8">
          <h2 className="text-2xl font-bold mb-4">Edit Device</h2>
          <input
            type="text"
            value={editDeviceName}
            onChange={(e) => setEditDeviceName(e.target.value)}
            placeholder="Device Name"
            className="border border-gray-300 rounded-md p-2 mb-4"
            />
            <select
              value={editDeviceStatus}
              onChange={(e) => setEditDeviceStatus(e.target.value)}
              className="border border-gray-300 rounded-md p-2 mb-4 w-1/6"
            >
                <option value="0">Off</option>
                <option value="1">On</option>
            </select>
            <button
              onClick={handleEditDevice}
              className="px-4 py-2 bg-blue-500 text-white rounded-md"
            >
                Save
            </button>
        </div>
        )}
        </div>
    );
};


export default function Page(){
    return (
        <SessionProvider>
            <AdminPage />
        </SessionProvider>
    );
};