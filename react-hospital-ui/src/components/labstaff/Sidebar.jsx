import { Link } from "react-router-dom";
import '../../css/doctor.css';

function Sidebar() {
    
    return (
        <aside className="sidebar">
            <p className="sidebar-header">AmazeCare</p>


            <Link to="/labstaff" className="nav-link">
                <i class="fa-solid fa-calendar"></i>
                Upload Reports
            </Link>

            <Link to="profile" className="nav-link">
                <i className="fa-solid fa-person" aria-hidden="true"></i>
                <span> Staff Profile</span>
            </Link>
            
        </aside>
    );
}

export default Sidebar;
