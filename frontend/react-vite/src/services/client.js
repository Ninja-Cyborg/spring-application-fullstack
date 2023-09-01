import axios from 'axios';

export const getPatrons = async () => {
    try{
       return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/patrons`)
    } catch(err){
        throw err;
    }
}

export const savePatron = async (patron) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/patrons`,
            patron
        )
    } catch (e) {
        throw e;
    }
}

export const updatePatron = async (id, update) => {
    try {
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/patrons/${id}`,
            update
        )
    } catch (e) {
        throw e;
    }
}

export const deletePatron = async (id) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/patrons/${id}`,
        )
    } catch (e) {
        throw e;
    }
}
