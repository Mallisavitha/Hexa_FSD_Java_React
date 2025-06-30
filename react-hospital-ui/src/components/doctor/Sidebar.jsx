import { Link } from "react-router-dom";
import '../../css/doctor.css';

function Sidebar() {
    
    return (
        <aside className="sidebar">
            <p className="sidebar-header">AmazeCare</p>

            <Link to="/doctor" className="nav-link">
                <i className="fa-regular fa-calendar" aria-hidden="true"></i>
                Dashboard
            </Link>

            <Link to="appointments/new" className="nav-link">
                <i className="fa-regular fa-calendar" aria-hidden="true"></i>
                New Appointments
            </Link>

            <Link to="appointments/upcoming" className="nav-link">
                <i className="fa-regular fa-calendar" aria-hidden="true"></i>
                Upcoming Appointments
            </Link>

            <Link to="appointments/past" className="nav-link">
                <i className="fa-regular fa-calendar" aria-hidden="true"></i>
                Past Appointments
    
            </Link>

            <Link to="slots" className="nav-link">
                <i className="fa fa-clone" aria-hidden="true"></i>
                Manage Slots
            </Link>


            <Link to="profile" className="nav-link">
                <i className="fa-solid fa-user-doctor" aria-hidden="true"></i>
                <span> My Profile</span>
            </Link>
            
        </aside>
    );
}

export default Sidebar;
