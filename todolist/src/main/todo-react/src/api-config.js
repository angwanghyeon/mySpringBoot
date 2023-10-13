let backendHost;

const hostName = window && window.location
    && window.location.hostname;

if (hostName === "localhost"){
    backendHost = "http://localhost:8282";
}
export const API_BASE_URL = `${backendHost}`;
