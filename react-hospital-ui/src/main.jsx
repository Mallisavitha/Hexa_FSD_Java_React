import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { Provider } from 'react-redux'
import store from './store/store.js'
import 'bootstrap/dist/css/bootstrap.min.css';

import 'primereact/resources/themes/lara-light-indigo/theme.css'; // Or your preferred theme
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';





createRoot(document.getElementById('root')).render(

    <Provider store={store}>
        <App />
    </Provider>

)
