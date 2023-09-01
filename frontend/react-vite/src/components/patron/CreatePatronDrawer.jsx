import {
    Button,
    Drawer, DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import CreatePatronForm from "./CreatePatronForm.jsx";

const AddIcon = () => "+";
const CloseIcon = () => "x";

const CreatePatronDrawer = () => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return <>
        <Button
            leftIcon={<AddIcon/>}
            colorScheme={"blackAlpha"}
            onClick={onOpen}
            >
            Create Patron
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"md"}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Create your account</DrawerHeader>

                <DrawerBody>
                    <CreatePatronForm
                        fetchPatrons={fetchPatrons}
                    />
                </DrawerBody>

                <DrawerFooter>
                    <Button
                        leftIcon={<CloseIcon/>}
                        colorScheme={"teal"}
                        onClick={onClose}>
                        Close
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
        </>
}

export default CreatePatronDrawer;