import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function UserList() {
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();
    const token = '185c7d65c4825310efe75d71222d16a039ffbecc5fc5066cce15185e5398bedf';

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        const res = await axios.get('https://gorest.co.in/public/v2/users');
        setUsers(res.data);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure to delete?')) {
            await axios.delete(`https://gorest.co.in/public/v2/users/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            fetchUsers();
        }
    };

    return (
        <div className="container" style={{ padding: "20px",marginLeft:"200px"}}>
            <h2>User Management Dashboard</h2>
            <button onClick={() => navigate('/users/new')} style={{ marginBottom: '20px',backgroundColor:"grey" }}>
                Add New User
            </button>
            <table border="3" width="100%" cellPadding="5">
                <thead>
                    <tr>
                        <th>Name</th><th>Email</th><th>Gender</th><th>Status</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(u => (
                        <tr key={u.id}>
                            <td>{u.name}</td>
                            <td>{u.email}</td>
                            <td>{u.gender}</td>
                            <td>{u.status}</td>
                            <td>
                                <button onClick={() => navigate(`/users/${u.id}`)} style={{backgroundColor:"grey"}}>Edit</button>{' '}
                                <button onClick={() => handleDelete(u.id)} style={{backgroundColor:"grey"}}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>

    )
}
export default UserList;