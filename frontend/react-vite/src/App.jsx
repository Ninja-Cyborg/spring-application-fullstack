import SidebarWithHeader from "./components/shared/Sidebar.jsx";
import { useEffect, useState } from 'react';
import { getPatrons} from "./services/client.js";
import { Wrap,
    WrapItem,
    Spinner,
    Text } from '@chakra-ui/react'
import CardWithImage from "./components/Card.jsx";
import CreatePatronDrawer from "./components/patron/CreatePatronDrawer.jsx";
import {errorNotification} from "./services/notification.js";

const App = () => {

    const[patrons, setPatrons] = useState([]);
    const[loading, setLoading] = useState(false);
    const[err, setError] = useState("");

    const fetchPatrons = () => {
        setLoading(true);
        getPatrons().then(res => {
            setPatrons(res.data)
        }).catch(err => {
            setError(err.response.data.message)
            errorNotification(
                err.code,
                err.response.data.message
            )
        }).finally(() => {
            setLoading(false)
        })
    }

    useEffect(() => {
        fetchPatrons();
    }, [])

    if(loading){
        return (
            <SidebarWithHeader>
                <Spinner color='red.500' />
            </SidebarWithHeader>
            )
    }

    if(err){
        return (
            <SidebarWithHeader>
                <CreatePatronDrawer
                    fetchPatrons = {fetchPatrons}
                />
                <Text mt={5}> There was an error!</Text>
            </SidebarWithHeader>
        )
    }

    if(patrons.length <= 0){
        return (
            <SidebarWithHeader>
                <Text> No patrons available</Text>
            </SidebarWithHeader>
        )
    }

  return (
      <SidebarWithHeader>
          <CreatePatornDrawer
            fetchPatron = {fetchPatrons}
          />
          <Wrap justify={"center"} spacing={"30px"}>
              {patrons.map((patron, index) =>{
                  <WrapItem key={index}>
                      <CardWithImage
                          {...patron}
                          randomImage = {index}
                          fetchPatrons={fetchPatrons}
                      />
                  </WrapItem>
              })}
          </Wrap>
      </SidebarWithHeader>
  )
}

export default App;