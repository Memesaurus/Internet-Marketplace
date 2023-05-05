import axios, { AxiosError } from "axios";

const baseURL = "http://localhost:8080/api";

const api = axios.create({
    baseURL: baseURL,
    withCredentials: true
});

const utilApi = axios.create({
    baseURL: baseURL,
    withCredentials: true
})


api.interceptors.request.use(async config => {
    if (!isCookiePresent("XSRF-TOKEN")) {
        console.log('xsrf token not present');
        
        await utilApi.get("/auth/csrf")
    }

    const token = getCsrfCookie("XSRF-TOKEN");
    
    api.defaults.headers.common['X-XSRF-TOKEN'] = token;

    return config;
});

const isCookiePresent = (key: string): boolean => {
    return Boolean(getRegex(key));
}

const getCsrfCookie = (key: string): string | undefined => {
    const ans = getRegex(key);

    return ans ? ans.pop() : undefined;
}

const getRegex = (key: string): RegExpMatchArray | null => document.cookie.match("(^|;)\\s*" + key + "\\s*=\\s*([^;]+)");

api.interceptors.response.use(
    response => response, 
    async error => {
        const lastRequest = error.config;

        
        if (error.status != 401 && isNotJWTExpiredError(error) && isNotStateRequest(error)) {                       
            return error;
        }        
        
        try {
            await utilApi.post("/auth/refresh")
        } catch (e) {            
            return e;
        }

        return api(lastRequest);
    }
)

const isNotJWTExpiredError = (error: AxiosError): boolean => {
    return !error.request?.response.includes("JWT expired");
}

const isNotStateRequest = (error: AxiosError): boolean => {
    return !error.config?.headers["STATE-REQUEST"];
}

export default api;