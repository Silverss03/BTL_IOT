"use client"
import React, {useState} from 'react';
import axios from 'axios';
import ProtectedRoute from 'components/ProtectedRoute';

type Item = {
    title: string;
    status: string;
};

//const [value, setValue] = useState('');
const items: Item[] = [
    { title: 'Bộ đọc thẻ', status: 'On' },
    { title: 'Cảm biến nhiệt độ', status: 'On' },
    { title: 'Màn hình LCD', status: 'On' },
    { title: 'LED 7 đoạn', status: 'On' },
];
    

const Page: React.FC = () => {
    return (
        <ProtectedRoute role="admin">
            <div className="flex flex-col gap-4 p-6 max-w-sm mx-auto">
                {items.map((item, index) => (
                    <div
                        key={index}
                        className="border border-gray-300 rounded-lg p-4 shadow-md bg-white"
                    >
                        <h3 className="text-lg font-bold mb-2">{item.title}</h3>
                        <p className="text-gray-700">Trạng thái: {item.status}</p>
                    </div>
                ))}
            </div>
        </ProtectedRoute>
    );
};

export default Page;