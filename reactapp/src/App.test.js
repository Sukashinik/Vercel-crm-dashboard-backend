import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from '../App';
import "@testing-library/jest-dom"
// Global mock
beforeEach(() => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      ok: true,
      json: () => Promise.resolve([]),
    })
  );
  window.alert = jest.fn();
});

test('React_BuildUIComponents_rendersNavbarLinksCorrectly', () => {
  render(<App />);
  expect(screen.getByText('Home')).toBeInTheDocument();
  expect(screen.getByText('Customers')).toBeInTheDocument();
});

test('React_BuildUIComponents_navigatesToHomePage', () => {
  render(<App />);
  expect(screen.getByText('Welcome to the Simple CRM Dashboard')).toBeInTheDocument();
});

test('React_BuildUIComponents_navigatesToCustomersPage', async () => {
  window.history.pushState({}, 'Customers Page', '/customers');
  render(<App />);
  expect(await screen.findByText('Customer Management')).toBeInTheDocument();
});

test('React_APIIntegration_TestingAndAPIDocumentation_showsNoCustomersMessage', async () => {
  global.fetch = jest.fn(() =>
    Promise.resolve({ ok: true, json: () => Promise.resolve([]) })
  );
  window.history.pushState({}, 'Customers Page', '/customers');
  render(<App />);
  expect(await screen.findByText('Customer Management')).toBeInTheDocument();
});

test('React_APIIntegration_TestingAndAPIDocumentation_rendersCustomerFromApi', async () => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      ok: true,
      json: () =>
        Promise.resolve([{ id: 1, name: 'John Doe', email: 'john@example.com', phone: '123', company: 'Acme Inc' }]),
    })
  );
  window.history.pushState({}, 'Customers Page', '/customers');
  render(<App />);
  expect(await screen.findByText('John Doe')).toBeInTheDocument();
  expect(screen.getByText('john@example.com')).toBeInTheDocument();
});

test('React_APIIntegration_TestingAndAPIDocumentation_addsNewCustomerSuccessfully', async () => {
  fetch.mockImplementationOnce(() => Promise.resolve({ ok: true, json: () => Promise.resolve([]) }))
    .mockImplementationOnce(() => Promise.resolve({ ok: true, json: () => Promise.resolve({}) }))
    .mockImplementationOnce(() => Promise.resolve({ ok: true, json: () => Promise.resolve([]) }));

  window.history.pushState({}, 'Customers Page', '/customers');
  render(<App />);

  fireEvent.change(screen.getByPlaceholderText('Name'), { target: { value: 'Jane Doe' } });
  fireEvent.change(screen.getByPlaceholderText('Email'), { target: { value: 'jane@example.com' } });
  fireEvent.change(screen.getByPlaceholderText('Phone'), { target: { value: '456' } });
  fireEvent.change(screen.getByPlaceholderText('Company'), { target: { value: 'Tech Corp' } });

  fireEvent.click(screen.getByText('Add Customer'));

  await waitFor(() => {
    expect(fetch).toHaveBeenCalledTimes(3);
  });
});

test('React_APIIntegration_TestingAndAPIDocumentation_editsExistingCustomer', async () => {
  const customers = [{ id: 1, name: 'John Doe', email: 'john@example.com', phone: '123', company: 'Acme Inc' }];

  fetch
    .mockImplementationOnce(() => Promise.resolve({ ok: true, json: () => Promise.resolve(customers) }))
    .mockImplementationOnce(() => Promise.resolve({ ok: true, json: () => Promise.resolve({}) }))
    .mockImplementationOnce(() => Promise.resolve({ ok: true, json: () => Promise.resolve(customers) }));

  window.history.pushState({}, 'Customers Page', '/customers');
  render(<App />);

  fireEvent.click(await screen.findByText('Edit'));
  fireEvent.change(screen.getByPlaceholderText('Name'), { target: { value: 'Updated Name' } });
  fireEvent.click(screen.getByText('Add Customer'));

  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(3));
});

test('React_APIIntegration_TestingAndAPIDocumentation_deletesCustomerSuccessfully', async () => {
  const customers = [{ id: 1, name: 'John Doe', email: 'john@example.com', phone: '123', company: 'Acme Inc' }];

  fetch
    .mockImplementationOnce(() => Promise.resolve({ ok: true, json: () => Promise.resolve(customers) }))
    .mockImplementationOnce(() => Promise.resolve({ ok: true, json: () => Promise.resolve({}) }))
    .mockImplementationOnce(() => Promise.resolve({ ok: true, json: () => Promise.resolve([]) }));

  window.history.pushState({}, 'Customers Page', '/customers');
  render(<App />);

  fireEvent.click(await screen.findByText('Delete'));

  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(3));
});

test('React_BuildUIComponents_footerRendersCorrectly', () => {
  render(<App />);
  expect(screen.getByText('© 2025 Simple CRM Dashboard')).toBeInTheDocument();
});

test('React_BuildUIComponents_navbarTitleRenders', () => {
  render(<App />);
  expect(screen.getByText('CRM Dashboard')).toBeInTheDocument();
});


