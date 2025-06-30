import axios from "axios";
import { useEffect, useState } from "react";

function StaffProfile() {
    const [lab, setLab] = useState(null);
    const [editMode, setEditMode] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);
    const [form, setForm] = useState({
        name: "",
        email: "",
        contact: "",
    });

    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchLab();
    }, []);

    const fetchLab = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/labstaff/get-one", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setLab(res.data);
            setForm({
                name: res.data.name || "",
                email: res.data.email || "",
                contact: res.data.contact || "",
            });
        } catch (err) {
            console.error("Error fetching Staff profile:", err);
        }
    };

    const handleImageChange = async (e) => {
        if (e.target.files && e.target.files[0]) {
            const image = e.target.files[0];
            setSelectedImage(image);

            const formData = new FormData();
            formData.append("file", image);

            try {
                const res = await axios.post(
                    `http://localhost:8080/api/labstaff/upload/profile-pic`,
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "multipart/form-data"
                        },
                    }
                );
                setLab(res.data);
                alert("Profile picture uploaded successfully!");
            } catch (err) {
                console.error("Image upload failed", err);
                alert("Failed to upload image");
            }
        }
    };

    const handleUpdate = async () => {
        try {
            const res = await axios.put(`http://localhost:8080/api/labstaff/update`, form, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setLab(res.data);
            setEditMode(false);
            alert("Profile updated successfully!");
        } catch (err) {
            console.log("Error updating profile:", err);
        }
    };

    return (
        <div className="container-fluid min-vh-100">
            <h4 className="mt-3">LabStaff Profile</h4>

            <div className="row mt-3">
                <div className="col-lg-12">
                    <div className="card shadow-sm p-4" style={{maxHeight:"98%"}}>
                        

                        <div className="card-body">
                            <div className="pic text-center">
                            {/* Profile Picture Display */}
                            <img
                                src={selectedImage? URL.createObjectURL(selectedImage) : lab?.profilePic? `../img/${lab.profilePic}`: "default.png"}
                                alt="Profile"
                                className="img-thumbnail mb-3"
                                style={{width: "150px",height: "150px",objectFit: "cover",borderRadius: "50%"}}/>
                        </div>
                            
                            {lab ? (
                                editMode ? (
                                    <div className="container">
                                        <div className="row mb-3">
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })}/>
                                            </div>
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Email" value={form.email} type="text" onChange={(e) => setForm({ ...form, email: e.target.value })}/>
                                            </div>
                                        </div>

                                        <div className="row mb-3">
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Contact" value={form.contact} onChange={(e) => setForm({ ...form, contact: e.target.value })}/>
                                            </div>
                                            </div>

                                        <div className="row mb-3">
                                            <div className="col-md-12">
                                                <label className="form-label">Upload Profile Picture</label>
                                                <input type="file" className="form-control" accept="image/*" onChange={handleImageChange} />
                                            </div>
                                        </div>

                                        <button className="btn btn-success" onClick={handleUpdate}>Save</button>
                                        <button className="btn btn-secondary ms-2" onClick={() => setEditMode(false)}>Cancel</button>
                                    </div>
                                ) : (
                                    <>
                                        <div className="row">
                                            <div className="col-md-6"><strong>Name:</strong> {lab.name}</div><br/><br/>
                                            <div className="col-md-6"><strong>DOB:</strong> {lab.email}</div><br/><br/>
                                            <div className="col-md-6"><strong>Contact:</strong> {lab.contact}</div><br/><br/>
                                        </div>
                                        <button className="btn btn-primary mt-3" onClick={() => setEditMode(true)}>Edit Profile</button>
                                    </>
                                )
                            ) : (
                                <p>Loading profile...</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default StaffProfile;
