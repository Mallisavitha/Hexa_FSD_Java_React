import React, { useEffect, useState } from 'react';
import '../../css/doctor.css';
import Sidebar from './Sidebar';
import Navbar from './Navbar';
import { Outlet, useNavigate } from 'react-router-dom';

function LabStaffDashboard() {
    
    const navigate = useNavigate();

    useEffect(() => {
        let token = localStorage.getItem('token');
        if (token == null || token == undefined || token == "")
            navigate("/")
    }, []);
    return (
        <div className="main-content" >
        <div className="d-flex">
            {/* Sidebar - fixed width */}
            <Sidebar />

            {/* Right side - navbar + page content */}
            <div className="flex-grow-1">
                <Navbar />
                <div className="container mt-4">
                    <Outlet/>
                </div>
            </div>
        </div>
        </div>
    );
}

export default LabStaffDashboard;
