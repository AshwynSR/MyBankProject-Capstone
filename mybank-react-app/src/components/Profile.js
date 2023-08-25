import { useAuth } from "./Auth"
import { useNavigate } from "react-router-dom"
import Navbar from "./NavBar"

export default function Profile() {

    const auth = useAuth()
    const navigate = useNavigate()

    const handleLogout = () => {
        auth.logout()
        navigate('/')
    }

    return(
        <div>
            <Navbar/>
            <h1>Welcome {auth.user.username}</h1>
            <button onClick={handleLogout}>Logout</button>
        </div>
    )
}