import React, { useState, useEffect} from "react";
import { Formik, Form, Field, FieldArray } from 'formik';
import * as Yup from 'yup';
import { useLocation, useNavigate, Link } from "react-router-dom";
import axios from "axios";
import Navbar from "./NavBar";

function Edit() {
  const location = useLocation();
  const navigate = useNavigate();
  const customerId = location.state.id;
  const [formData, setFormData] = useState(null);
  const accountTypes = ['Current', 'Savings', 'Salary'];

  useEffect(() => {
    axios
      .get(`http://localhost:8080/customers/${customerId}`)
      .then((response) => {
        setFormData(response.data);
        console.log(response.data);
      })
  }, [customerId]);

  const validationSchema = Yup.object().shape({
    name: Yup.string().required('Name is required'),
    email: Yup.string().email('Invalid email address').required('Email is required'),
    address: Yup.string().required('Address is required'),
    accounts: Yup.array().of(
      Yup.object().shape({
        accountType: Yup.string().oneOf(accountTypes, 'Invalid account type').required('Account Type is required'),
        balance: Yup.number().typeError('Balance must be a number').required('Balance is required').min(0, 'Balance must be greater than or equal to 0'),
      })
    ),
  });

  const handleDeleteAccounts = () => {
    const customerId  = location.state.id; 
    axios.delete(`http://localhost:8080/customers/${customerId}/accounts`)
    .then((response) => {
      alert('Response: ' + response.data);
    })
    navigate('/list')
  }

  const handleSubmit = (values) => {
    console.log(values);
    const customerId  = location.state.id; 
    axios
      .put(`http://localhost:8080/customers/${customerId}`, values)
      .then((response) => {
        alert('Response: ' + response.data);
      })
      .catch((error) => {
        alert('Error: ' + error.message);
      });
      navigate('/list')
  };

  return (
    <div>
      <Navbar />
      <h1>Edit Employee</h1>
      {formData && ( 
        <>
          <Formik
            initialValues={formData}
            onSubmit={handleSubmit}
            validationSchema={validationSchema}
          >
            <Form>
              <div>
                <label htmlFor="name">Name:</label>
                <Field type="text" id="name" name="name" />
              </div>
              <div>
                <label htmlFor="email">Email:</label>
                <Field type="email" id="email" name="email" />
              </div>
              <div>
                <label htmlFor="address">Address:</label>
                <Field type="text" id="address" name="address" />
              </div>
              <div>
                <label htmlFor="accounts">Accounts</label>
                <FieldArray name="accounts">
                  <div>
                    {formData.accounts.map((account, index) => (
                      <div key={index}>
                        <label>Account ID: {account.id}</label>

                        <label htmlFor={`accounts.${index}.accountType`}>Account Type:</label>
                        <Field as="select" name={`accounts.${index}.accountType`}>
                          <option value="Current">Current</option>
                          <option value="Savings">Savings</option>
                          <option value="Salary">Salary</option>
                        </Field>
                        <label htmlFor={`accounts.${index}.balance`}>Balance:</label>
                        <Field type="number" name={`accounts.${index}.balance`} />
                      </div>
                    ))}
                  </div>
                  
                </FieldArray>
              </div>
              
              <button className="save" type="submit">Save Edit</button>
            </Form>
          </Formik>
          <button><Link to={`/addAccount/${customerId}`}>Add Account</Link></button>
          
          <button className="delete" type="submit" onClick={handleDeleteAccounts}>Delete All Accounts</button>
        </>
      )}
    </div>
  );
}

export default Edit;