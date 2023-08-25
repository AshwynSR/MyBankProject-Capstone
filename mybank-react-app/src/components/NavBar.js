import { Link } from "react-router-dom"

export default function Navbar(){
    return (
        <>
            <nav>
                <h1>Bank Management System</h1>
                <ul>
                <li><Link to="/home">Home</Link> </li>
                <li><Link to="/list">Customer List</Link> </li>
                <li><Link to="/add">Add Customer</Link></li>
                <li><Link to="/transfer">Transfer Amount</Link></li>
                
                <li><Link to="/profile">Profile</Link></li>
                </ul>
            </nav>
        </>
    )
}