import axios from 'axios';

export const getPatrons = async () => {
    try{
       return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/patrons`)
    } catch(err){
        throw err;
    }
}