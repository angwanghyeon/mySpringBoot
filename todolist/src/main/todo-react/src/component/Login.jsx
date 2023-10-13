import React from 'react'
import {Container, Grid, Typography} from "@mui/material";

const Login = () => {



    return (
        <Container component="main" maxWidth="xs" style={{marginTop:"8%"}}>
            <Grid item xs={12}>
                <Typography component="h1" variant='h5'>
                    로그인
                </Typography>
            </Grid>
            <form noValidate onSubmit={handleSubmit}>
                {"  "}

            </form>
        </Container>
    )
}
export default Login
