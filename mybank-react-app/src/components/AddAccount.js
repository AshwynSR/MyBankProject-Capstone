import axios from "axios";
import { ErrorMessage, Field, Form, Formik } from 'formik';
import { useNavigate, useParams } from 'react-router-dom';
import * as Yup from 'yup';
import Navbar from "./NavBar";

export default function AddAccount(){
    const accountTypes = ['Current', 'Savings', 'Salary'];
    const  { Id }  = useParams();
    // const location = useLocation();
    const navigate = useNavigate();
    // const customerId = location.state.id;
    const validationSchema = Yup.object().shape({
            accountType: Yup.string().oneOf(accountTypes, 'Invalid account type').required('Account Type is required'),
            balance: Yup.number().typeError('Balance must be a number').required('Balance is required').min(0, 'Balance must be greater than or equal to 0'),
        });
    const handleSubmit = (values) =>{
        const customerId = Id;
        axios.post(`http://localhost:8080/customers/${customerId}/accounts`, values)
        .then(response => {
            alert(response.data)
            navigate("/edit", {state: {id:customerId}})
        })
    }
    
    return(
        <div>
        <Navbar />
        <Formik initialValues={{accountType:'', balance:0}} validationSchema={validationSchema} onSubmit={handleSubmit}>
            <Form>
                <div>
                    <Field as="select" id="accountType" name="accountType">
                        <option value="">Select an account type</option>
                        {accountTypes.map((type) => (
                            <option key={type} value={type}>
                            {type}
                            </option>
                        ))}
                    </Field>
                    <ErrorMessage name="accountType" component="div" />

                    <label htmlFor="balance">Balance:</label>
                    <Field
                    type="number"
                    id="balance"
                    name="balance"
                    />
                    <ErrorMessage name="balance" component="div" />
                </div>
                <button className='save' type="submit">Submit</button>
            </Form>
            
        </Formik>
        </div>
        
    )
}