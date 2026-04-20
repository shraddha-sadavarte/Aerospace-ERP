import axios from 'axios';

const api = axios.create({
    beaseURL: 'http://localhost:8081/api/v1',
});

export default api;