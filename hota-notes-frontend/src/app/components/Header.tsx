'use client';

import { Title, Flex } from '@mantine/core';

export default function Header() {
  return (
    <Flex justify="space-between" align="center" p="md">
      <Title order={2}>HotaNotes</Title>
    </Flex>
  );
}
