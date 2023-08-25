import React, {useEffect, useState} from "react";
import {Link,useNavigate } from "react-router-dom"

import axios from "axios"
import Navbar from "./NavBar";

function List(){
    const [customers, setCustomers] = useState([]);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("http://localhost:8080/customers")
      .then((response) => setCustomers(response.data))
      .catch((error) => {
        alert("No customer data");
        navigate("/");
      });
  }, []);
    
    
    
    function handleDelete(id) {
        axios.delete(`http://localhost:8080/customers/${id}`)
        .then((response) => {
            window.location.reload();
          })
    }
      
    function handleEdit(id){
        navigate("/edit", {state: {id:id}})
    }

    return (
        <div>
            <Navbar />
            <h1>Customer List</h1>
            <input className="search" placeholder="Search Customer" onChange={(e)=>setSearch(e.target.value )} />
            <p>Total Number of customers: {customers.length}</p>
            <table>
                <thead>
                    <tr>
                        <th>Customer ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Address</th>
                        <th>Bank Accounts</th>
                        <th>Edit/Delete</th>
                    </tr>
                </thead>
                <tbody>
                    
                    {customers
                    .filter((contact)=> {
                        return search.toLowerCase() === "" ?
                         contact : contact.name.toLowerCase().includes(search.toLowerCase()) 
                         || contact.email.toLowerCase().includes(search.toLowerCase()) 
                    })
                    .map((contact) =>(
                        <tr key={contact.id}>
                            <th><Link to={`/customer/${contact.id}`}>{contact.id}</Link></th>
                            <th>{contact.name}</th>
                            <th>{contact.email}</th>
                            <th>{contact.address}</th>
                            <th>{contact.accounts.map((e) => (
                                <p>{e.id}</p>
                                ))}
                            </th>
                            
                            <th>
                                <button className="save" type="button" onClick={() => handleEdit(contact.id)}>Edit</button>
                                <button className="delete" type="button" onClick={() => handleDelete(contact.id)}>Delete</button>
                            </th>

                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}
export default List;