import {Box, Typography} from "@mui/material";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import App from "../App";
import Login from "./Login";
import Signup from "./Signup";

function Copyright() {
    return  (
        <Typography variant="body2" color="textSecondary" align="center">
            {new Date().getFullYear()} {". Copyright @ "}한국 정보 교육원 3조 JMT
        </Typography>
    )

}

function AppRouter() {
    return (
        <div>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<App />}></Route>
                    <Route path="login" element={<Login />}></Route>
                    <Route path="signup" element={<Signup />}></Route>
                </Routes>
            </BrowserRouter>
            <Box mt={5}>
                <Copyright></Copyright>
            </Box>
        </div>
    )
}

export default AppRouter;