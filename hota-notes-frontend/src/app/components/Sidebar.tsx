'use client';

import { Box, NavLink, Stack } from '@mantine/core';
import { IconPlus } from '@tabler/icons-react';

import { HiOutlineHome } from "react-icons/hi2";
import { PiNotePencil, PiFolders } from "react-icons/pi";

import Link from 'next/link';

export default function Sidebar() {
  return (
    <Box w={250} p="md" style={{ borderRight: '1px solid #eaeaea', height: '100vh' }}>
      <Stack>
        <h1>HotaNotes</h1>
        <NavLink
          component={Link}
          href="/home"
          label="Home"
          leftSection={<HiOutlineHome size={20} />}
        />
        <NavLink
          component={Link}
          href="/notes/new"
          label="New Note"
          leftSection={<IconPlus size={20} />}
        />
        <NavLink
          component={Link}
          href="/notes"
          label="My Notes"
          leftSection={<PiNotePencil size={20} />}
        />
        <NavLink
          component={Link}
          href="/organise"
          label="Organise"
          leftSection={<PiFolders size={20} />}
        />
      </Stack>
    </Box>
  );
}
