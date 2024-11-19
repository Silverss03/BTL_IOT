"use client"
import React, {useState, useEffect} from 'react';
import { SessionProvider, useSession } from 'next-auth/react';
import Switch from 'react-switch';

interface Item{
    device_name: string;
    device_status: string;
};
    
function AdminPage(){
    const [items, setItems] = useState<Item[]>([]);
    const { data: session, status } = useSession();

    async function getDevice(): Promise<Item[]> {
        const res = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/getDevice`, {
            cache: 'no-store', // Ensures fresh data on each request if needed
        });
        const data = await res.json();
        console.log('Fetched data:', data); // Log the fetched data
        return Array.isArray(data) ? data : data.classes || [];;
    }

    const handleToggle = async (index: number) => {
        const updatedItems = [...items];
        updatedItems[index].device_status = updatedItems[index].device_status === '1' ? '0' : '1';
        setItems(updatedItems);
    
        // Optionally, you can send a request to update the device status in the backend
        // await fetch(`localhost:1883/api/updateDeviceStatus`, {
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json',
        //     },
        //     body: JSON.stringify({
        //         device_name: updatedItems[index].device_name,
        //         device_status: updatedItems[index].device_status,
        //     }),
        // });
    };

    useEffect(() => {
        if (status === 'loading') return; // Do nothing while loading
        async function fetchData() {
          const result = await getDevice();
          setItems(result);
        }
        fetchData();
      }, [session, status]);

    return (
        <div className="flex flex-col gap-4 p-6 max-w-sm mx-auto">
            {items.map((item, index) => (
                <div
                    key={index}
                    className="border border-gray-300 rounded-lg p-4 shadow-md bg-white"
                >
                    <h3 className="text-lg font-bold mb-2">{item.device_name}</h3>
                    <p className="text-gray-700">Trạng thái: {item.device_status == '1'? 'Bật' : 'Tắt'}</p>
                    <Switch
                    onChange={() => handleToggle(index)}
                    checked={item.device_status == '1'}
                    offColor="#888"
                    onColor="#0f0"
                    />
                </div>
            ))}
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