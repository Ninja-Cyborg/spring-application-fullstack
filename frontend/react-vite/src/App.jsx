import SidebarWithHeader from "./components/shared/Sidebar.jsx";
import { useEffect, useState } from 'react';
import { getPatrons} from "./services/client.js";
import { Wrap,
    WrapItem,
    Spinner,
    Text } from '@chakra-ui/react'
import CardWithImage from "./components/Card.jsx";

const App = () => {

    const[patrons, setPatrons] = useState([]);
    const[loading, setLoading] = useState(false);


    useEffect(() => {
        setLoading(true);
        getPatrons().then(res =>{
            setPatrons(res.data)
        }).catch(err =>{
            console.log(err)
        }).finally( () => {
            setLoading(false)
        })
    }, []);

    if(loading){
        return (
            <SidebarWithHeader>
                <Spinner color='red.500' />
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
          <Wrap justify={"center"} spacing={"30px"}>
              {patrons.map((patron, index) =>{
                  <WrapItem key={index}>
                      <CardWithImage {...patron}/>
                  </WrapItem>
              })}
          </Wrap>
      </SidebarWithHeader>
  )
}

export default App;