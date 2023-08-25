import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import Home from "./components/Home";
import List from "./components/List";
import Form from "./components/Form"
import CustomerDetail from "./components/CustomerDetail";
import Transfer from "./components/Transfer";
import Edit from "./components/Edit"
import AddAccount from "./components/AddAccount";
import LoginForm from "./components/LoginForm";
import { AuthProvider } from "./components/Auth";
import { RequireAuth } from "./components/RequireAuth";
import Profile from "./components/Profile";


function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<LoginForm />} />
          <Route path="/home" element={<RequireAuth><Home /></RequireAuth>} />
          <Route path="/list" element={<RequireAuth><List /></RequireAuth>} />
          <Route path="/add" element={<RequireAuth><Form /></RequireAuth>} />
          <Route path="/transfer" element={<RequireAuth><Transfer /></RequireAuth>} />
          <Route path="/customer/:Id" element={<RequireAuth><CustomerDetail /></RequireAuth>} />
          <Route path="/edit" element={<RequireAuth><Edit /></RequireAuth>} />
          <Route path="/addAccount/:Id" element={<RequireAuth><AddAccount /></RequireAuth>} />
          <Route path="/profile" element={<RequireAuth><Profile /></RequireAuth>} />
        </Routes>
      </Router>
    </AuthProvider>
    
  );
}

export default App;
