import Navbar from "components/navbar";
import Home from "pages/Home";
import Catalog from "pages/Catalog";
import Admin from "pages/Admin";

import { BrowserRouter, Switch, Route } from "react-router-dom";

const Routes = () => (
        <BrowserRouter>
        <Navbar />
            <Switch>
                <Route path="/" exact>
                    <Home/>
                </Route>
                <Route path="/products">
                    <Catalog/>
                </Route>
                <Route path="/admin">
                    <Admin/>
                </Route>
            </Switch>
        
        </BrowserRouter>
    );

export default Routes;