import axios from "axios";
import { useEffect, useState } from "react";

function Profile() {
    const [receptionist, setReceptionist] = useState(null);
    const [editMode, setEditMode] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);
    const [form, setForm] = useState({
        name: "",
        phone: "",
        email: "",
    });

    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchReceptionistProfile();
    }, []);

    const fetchReceptionistProfile = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/receptionist/get-one", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setReceptionist(res.data);
            setForm({
                name: res.data.name || "",
                phone: res.data.phone || "",
                email: res.data.email || "",
            });
        } catch (err) {
            console.error("Error fetching doctor profile:", err);
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
                    "http://localhost:8080/api/receptionist/upload/profile-pic",
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "multipart/form-data"
                        },
                    }
                );
                setReceptionist(res.data);
                alert("Profile picture uploaded successfully!");
            } catch (err) {
                console.error("Image upload failed", err);
                alert("Failed to upload image");
            }
        }
    };

    const handleUpdate = async () => {
        try {
            const res = await axios.put("http://localhost:8080/api/receptionist/update", form, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setReceptionist(res.data);
            setEditMode(false);
            alert("Profile updated successfully!");
        } catch (err) {
            console.error("Error updating profile:", err);
            alert("Failed to update profile.");
        }
    };

    return (
        <div className="container-fluid min-vh-100">
            <h4 className="mt-3">Receptionist Profile</h4>

            <div className="row mt-5">
                <div className="col-lg-12">
                    <div className="card shadow-sm p-4" style={{maxHeight:"98%"}}>
                        

                        <div className="card-body">
                            <div className="pic text-center">
                            {/* Profile Picture Display */}
                            <img
                                src={
                                    selectedImage
                                        ? URL.createObjectURL(selectedImage)
                                        : receptionist?.profilePic
                                            ? `../img/${receptionist.profilePic}`
                                            : "../imag/profile_pic.png"
                                }
                                alt="Profile"
                                className="img-thumbnail mb-3"
                                style={{
                                    width: "150px",
                                    height: "150px",
                                    objectFit: "cover",
                                    borderRadius: "50%"
                                }}
                            />
                        </div>
                            
                            {receptionist ? (
                                editMode ? (
                                    <div className="container">
                                        <div className="row mb-3">
                                            <div className="col-md-6">
                                                <input
                                                    className="form-control"
                                                    placeholder="Name"
                                                    value={form.name}
                                                    onChange={(e) => setForm({ ...form, name: e.target.value })}
                                                />
                                            </div>
                                            <div className="col-md-6">
                                                <input
                                                    className="form-control"
                                                    placeholder="Contact"
                                                    value={form.phone}
                                                    onChange={(e) => setForm({ ...form, phone: e.target.value })}
                                                />
                                            </div>
                                        </div>

                                        <div className="row mb-3">
                                            <div className="col-md-6">
                                                <input
                                                    className="form-control"
                                                    placeholder="Email"
                                                    value={form.email}
                                                    onChange={(e) => setForm({ ...form, email: e.target.value })}
                                                />
                                            </div>
                                            
                                        </div>

                                        

                                        <div className="row mb-3">
                                            <div className="col-md-12">
                                                <label className="form-label">Upload Profile Picture</label>
                                                <input
                                                    type="file"
                                                    className="form-control"
                                                    accept="image/*"
                                                    onChange={handleImageChange}
                                                />
                                            </div>
                                        </div>

                                        <button className="btn btn-success" onClick={handleUpdate}>Save</button>
                                        <button className="btn btn-secondary ms-2" onClick={() => setEditMode(false)}>Cancel</button>
                                    </div>
                                ) : (
                                    <>
                                        <div className="row">
                                            <div ><strong>Name:</strong> {receptionist.name}</div><br/><br/>
                                            <div><strong>Contact:</strong> {receptionist.phone}</div><br/><br/>
                                            <div ><strong>Email:</strong> {receptionist.email}</div><br/><br/>
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

export default Profile;
