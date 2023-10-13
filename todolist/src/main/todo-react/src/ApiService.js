import {API_BASE_URL} from "./api-config";

export function call(api, method, request){

    let headers = new Headers({
        "Content-Type" : "application/json"
    });

    const accessToken = localStorage.getItem("ACCESS_TOKEN");
    if (accessToken && accessToken != null){
        headers.append("Authorization", "Bearer " + accessToken);
    }

    let options = {
        headers : headers,
        url : API_BASE_URL + api,
        method : method,
    };

    if (request){
        options.body = JSON.stringify(request);
    }

    return fetch(options.url, options).then((response) => {
            if (response.status === 200){
                return response.json();
            }else if (response.status === 403){
                window.location.href = "/login";
            }else {
                throw Error(response);
            }
            }).catch((error) => {
            console.log(error);
        });
}

export function signin(userDto){
    return call("/auth/signin", "POST", userDto)
        .then((response) => {
            localStorage.setItem("ACCESS_TOKEN", response.token);
            console.log("response : ", response);
            window.location.href = "/";
        })
}

export function signout(){
    localStorage.setItem("ACCESS_TOKEN", null);
    window.location.href = "/login";
}

export function signup(userDto) {
    return call("/auth/signup", "POST", userDto);
}