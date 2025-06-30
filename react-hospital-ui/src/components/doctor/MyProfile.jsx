import axios from "axios";
import { useEffect, useState } from "react";

function MyProfile() {
    const [doctor, setDoctor] = useState(null);
    const [editMode, setEditMode] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);
    const [form, setForm] = useState({
        fullName: "",
        contact: "",
        specialization: "",
        experienceYear: "",
        qualification: "",
        designation: ""
    });

    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchDoctorProfile();
    }, []);

    const fetchDoctorProfile = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/doctor/get-one", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setDoctor(res.data);
            setForm({
                fullName: res.data.fullName || "",
                contact: res.data.contact || "",
                specialization: res.data.specialization || "",
                experienceYear: res.data.experienceYear || "",
                qualification: res.data.qualification || "",
                designation: res.data.designation || ""
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
                    "http://localhost:8080/api/doctor/upload/profile-pic",
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "multipart/form-data"
                        },
                    }
                );
                setDoctor(res.data);
                alert("Profile picture uploaded successfully!");
            } catch (err) {
                console.error("Image upload failed", err);
                alert("Failed to upload image");
            }
        }
    };

    const handleUpdate = async () => {
        try {
            const res = await axios.put("http://localhost:8080/api/doctor/update", form, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setDoctor(res.data);
            setEditMode(false);
            alert("Profile updated successfully!");
        } catch (err) {
            console.error("Error updating profile:", err);
            alert("Failed to update profile.");
        }
    };

    return (
        <div className="container-fluid min-vh-100">
            <h4 className="mt-3">Doctor Profile</h4>

            <div className="row mt-3">
                <div className="col-lg-12">
                    <div className="card shadow-sm p-4" style={{maxHeight:"98%"}}>
                        

                        <div className="card-body">
                            <div className="pic text-center">
                            {/* Profile Picture Display */}
                            <img src={ selectedImage ? URL.createObjectURL(selectedImage)
                                : doctor?.profilePic ? `../img/${doctor.profilePic}` : "../img/profile_pic.png"}
                                alt="Profile" className="img-thumbnail mb-3"
                                style={{ width: "150px", height: "150px", objectFit: "cover", borderRadius: "50%" }}/>
                        </div>
                            
                            {doctor ? (
                                editMode ? (
                                    <div className="container">
                                        <div className="row mb-3">
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Name" value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })}/>
                                            </div>
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Contact" value={form.contact} onChange={(e) => setForm({ ...form, contact: e.target.value })}/>
                                            </div>
                                        </div>

                                        <div className="row mb-3">
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Specialization" value={form.specialization} onChange={(e) => setForm({ ...form, specialization: e.target.value })}/>
                                            </div>
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Qualification" value={form.qualification} onChange={(e) => setForm({ ...form, qualification: e.target.value })}/>
                                            </div>
                                        </div>

                                        <div className="row mb-3">
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Designation" value={form.designation} onChange={(e) => setForm({ ...form, designation: e.target.value })}/>
                                            </div>
                                            <div className="col-md-6">
                                                <input type="number" className="form-control" placeholder="Experience" value={form.experienceYear} onChange={(e) => setForm({ ...form, experienceYear: e.target.value })}/>
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
                                            <div className="col-md-6"><strong>Name:</strong> {doctor.fullName}</div><br/><br/>
                                            <div className="col-md-6"><strong>Specialization:</strong> {doctor.specialization}</div><br/><br/>
                                            <div className="col-md-6"><strong>Contact:</strong> {doctor.contact}</div><br/><br/>
                                            <div className="col-md-6"><strong>Qualification:</strong> {doctor.qualification}</div><br/><br/>
                                            <div className="col-md-6"><strong>Designation:</strong> {doctor.designation}</div><br/><br/>
                                            <div className="col-md-6"><strong>Experience:</strong> {doctor.experienceYear} years</div><br/><br/>
                                            <div className="col-md-6"><strong>Department:</strong> {doctor.department?.name}</div><br/><br/>
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

export default MyProfile;
