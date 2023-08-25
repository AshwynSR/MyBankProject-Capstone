import axios from "axios";
import { ErrorMessage, Field, FieldArray, Form, Formik } from 'formik';
import { useNavigate } from 'react-router-dom';
import * as Yup from 'yup';
import Navbar from "./NavBar";
 
const MyForm = () => {
  const accountTypes = ['Current', 'Savings', 'Salary'];
  const navigate = useNavigate();

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

  const handleSubmit = (values) => {
    axios.post("http://localhost:8080/customers",values)
    .then((response) => {
      navigate("/list"); // Navigate after successful POST request
    })
  };

  return (
    <>
    <Navbar />
    <h1>Add Customer</h1>
    <Formik
      className='input-form'
      initialValues={{
        name: '',
        email: '',
        address: '',
        accounts: [{ accountType: '', balance: '' }],
      }}
      validationSchema={validationSchema}
      onSubmit={handleSubmit}
    >
      
      {({ values }) => (
        <Form>
          <div>
            <label htmlFor="name">Name:</label>
            <Field type="text" id="name" name="name" />
            <ErrorMessage name="name" component="div" />
          </div>
          <div>
            <label htmlFor="email">Email:</label>
            <Field type="email" id="email" name="email" />
            <ErrorMessage name="email" component="div" />
          </div>
          <div>
            <label htmlFor="address">Address:</label>
            <Field type="text" id="address" name="address" />
            <ErrorMessage name="address" component="div" />
          </div>
          <div>
            <label htmlFor="accounts">Account Type:</label>
            <FieldArray name="accounts">
              {({ push, remove }) => (
                <div>
                  {values.accounts.map((account, index) => (
                    <div key={index}>
                      <label htmlFor={`accounts.${index}.accountType`}>Account Type:</label>
                      <Field as="select" id={`accounts.${index}.accountType`} name={`accounts.${index}.accountType`} className="dropdown">
                        <option value="">Select an account type</option>
                        {accountTypes.map((type) => (
                          <option key={type} value={type}>
                            {type}
                          </option>
                        ))}
                      </Field>
                      <ErrorMessage name={`accounts.${index}.accountType`} component="div" />

                      <label htmlFor={`accounts.${index}.balance`}>Balance:</label>
                      <Field
                        type="number"
                        id={`accounts.${index}.balance`}
                        name={`accounts.${index}.balance`}
                      />
                      <ErrorMessage name={`accounts.${index}.balance`} component="div" />

                      <button className='delete' type="button" onClick={() => remove(index)}>
                        Remove Account
                      </button>
                    </div>
                  ))}
                  <button type="button" onClick={() => push({ accountType: '', balance: '' })}>
                    Add Account
                  </button>
                </div>
              )}
            </FieldArray>
          </div>
          <button className='save' type="submit">Submit</button>
        </Form>
      )}
    </Formik>
    </>
  );
};

export default MyForm;
