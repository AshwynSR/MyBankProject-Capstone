import React from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import axios from "axios";
import Navbar from "./NavBar";

const Transfer = () => {
  const initialValues = {
    fromAccount: "",
    toAccount: "",
    amount: "",
  };

  const validationSchema = Yup.object().shape({
    fromAccount: Yup.number()
      .required("From Account is required")
      .positive("From Account must be a positive number")
      .notOneOf([Yup.ref("toAccount")], "From Account and To Account can't be the same"),
    toAccount: Yup.number()
      .required("To Account is required")
      .positive("To Account must be a positive number"),
    amount: Yup.number()
      .required("Amount is required")
      .positive("Amount must be a positive number"),
  });

  const handleSubmit = (values, { resetForm }) => {
    const requestData = new URLSearchParams();
    requestData.append("fromAccountId", values.fromAccount);
    requestData.append("toAccountId", values.toAccount);
    requestData.append("amount", values.amount);
  
    axios
      .post('http://localhost:8080/customers/transfer-funds', requestData, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      })
      .then((response) => {
        alert('Response: ' + response.data);
        resetForm();
      })
      .catch((error) => {
        alert('Error: ' + error.message);
      });
  };

  return (
    <>
    <Navbar />
    <h1>Transfer Funds</h1>
    <Formik initialValues={initialValues} onSubmit={handleSubmit} validationSchema={validationSchema}>
      <Form>
        <div>
            <label htmlFor="fromAccount">From Account:</label>
            <Field type="number" id="fromAccount" name="fromAccount" />
            <ErrorMessage name="fromAccount" component="div" />
        </div>
        <div>
            <label htmlFor="toAccount">To Account:</label>
            <Field type="number" id="toAccount" name="toAccount" />
            <ErrorMessage name="toAccount" component="div" />
        </div>
        <div>
            <label htmlFor="amount">Amount:</label>
            <Field type="number" id="amount" name="amount" />
            <ErrorMessage name="amount" component="div" />
        </div>
        <button type="submit">Submit</button>
      </Form>
    </Formik>
    </>
  );
};

export default Transfer;
