'use client';

import { Container, Flex, Text } from '@mantine/core';
import Header from './components/Header';
import Sidebar from './components/Sidebar';

export default function Home() {
  return (
    <Flex>
      <Sidebar />
      <Container fluid>
        <Header />
        <Text>Welcome to HotaNotes</Text>
      </Container>
    </Flex>
  );
}
