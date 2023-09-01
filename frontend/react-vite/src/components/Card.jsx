import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Tag,
    useColorModeValue, useDisclosure,
} from '@chakra-ui/react';

import {useRef} from 'react'
import {deletePatron} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";
import UpdatePatronDrawer from "./patron/UpdatePatronDrawer.jsx";

export default function CardWithImage({id, name, email, age, gender, image}) {
    const randomGender = gender === "MALE" ? "men" : "women";
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = useRef()

    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                w={'full'}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'2xl'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        `https://randomuser.me/api/portraits/thumb/${randomGender}/${randomImage}.jpg`
                    }
                    objectFit={'cover'}
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ'
                        }
                        alt={'Author'}
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={2} align={'center'} mb={5}>
                        <Tag borderRadius={"full"}>{id}</Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                        <Text color={'gray.500'}>Age {age} | {gender}</Text>
                    </Stack>
                </Box>

                <Stack direction={'row'} justify={'center'} spacing={6} p={4}>
                    <Stack>
                        <UpdatePatronDrawer
                            initialValues={{ name, email, age }}
                            patronId={id}
                            fetchPatrons={fetchPatrons}
                        />
                    </Stack>
                    <Stack>
                        <Button
                            bg={'red.400'}
                            color={'white'}
                            rounded={'full'}
                            _hover={{
                                transform: 'translateY(-2px)',
                                boxShadow: 'lg'
                            }}
                            _focus={{
                                bg: 'green.500'
                            }}
                            onClick={onOpen}
                        >
                            Delete
                        </Button>
                        <AlertDialog
                            isOpen={isOpen}
                            leastDestructiveRef={cancelRef}
                            onClose={onClose}
                        >
                            <AlertDialogOverlay>
                                <AlertDialogContent>
                                    <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                                        Delete Patron
                                    </AlertDialogHeader>

                                    <AlertDialogBody>
                                        Are you sure you want to delete {name}? You can't undo this action afterwards.
                                    </AlertDialogBody>

                                    <AlertDialogFooter>
                                        <Button ref={cancelRef} onClick={onClose}>
                                            Cancel
                                        </Button>
                                        <Button colorScheme='red' onClick={() => {
                                            deletePatron(id).then(res => {
                                                console.log(res)
                                                successNotification(
                                                    'Patron deleted',
                                                    `${name} was successfully deleted`
                                                )
                                                fetchPatrons();

                                            }).catch(err => {
                                                console.log(err);
                                                errorNotification(
                                                    err.code,
                                                    err.response.data.message
                                                )
                                            }).finally(() => {
                                                onClose()
                                            })
                                        }} ml={3}>
                                            Delete
                                        </Button>
                                    </AlertDialogFooter>
                                </AlertDialogContent>
                            </AlertDialogOverlay>
                        </AlertDialog>
                    </Stack>

                </Stack>
            </Box>
        </Center>
    );
}