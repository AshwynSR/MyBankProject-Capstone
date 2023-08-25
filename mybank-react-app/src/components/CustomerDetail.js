import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import Navbar from "./NavBar";


function CustomerDetail(){
    const  { Id }  = useParams();
    const navigate = useNavigate();
    const [customer, setCustomer] = useState({id:'', name:'', email:'', address:'', accounts: []});
    const url = `http://localhost:8080/customers/${Id}`;
    useEffect(() => {
        axios.get(url)
        .then(response => {setCustomer(response.data)})
        // .catch(error => setIsError(error.message))
        
    },[url])

    function handleBack(){
        navigate('/list')
    }
    
    return (
      <div>
        <Navbar />
        <h1>Customer Details</h1>
        <p>ID: {Id}</p>
        <p>Name: {customer.name}</p>
        <p>Email: {customer.email}</p>
        <p>Address: {customer.address}</p>
        <div>Accounts: 
                <table><tbody>
                {customer.accounts.map((account) => (
                    <tr key={account.id}>
                        <td>Account Id: {account.id}</td>
                        <td>Account Type: {account.accountType}</td>
                        <td>Balance: {account.balance}</td>
                    </tr>
                    ))}
                    </tbody>
                </table>
            
        </div>
        <button type="submit" onClick={handleBack}>Back</button>
      </div>
    );
  }

export default CustomerDetail;