import { describe, it, expect } from 'vitest'
import { render, screen, within } from '@testing-library/react'
import ShipmentCard from '../../../src/components/Shipment/ShipmentCard'

describe('ShipmentCard', () => {
    const mockShipment = {
    id: 1,
    pedidoId: 10,
    direccion: 'Av. Principal 123',
    estado: 'PENDIENTE',
    cliente: 'Juan Perez',
    producto: 'Laptop Dell',
    cantidad: 2,
    total: 1999.98,
    }

    it('renderiza el id del envio', () => {
    render(<ShipmentCard shipment={mockShipment} />)
    expect(screen.getByText('Envío #1')).toBeInTheDocument()
    })

    it('renderiza el id del pedido', () => {
    render(<ShipmentCard shipment={mockShipment} />)
    expect(screen.getByText('#10')).toBeInTheDocument()
    })

    it('renderiza la direccion', () => {
    render(<ShipmentCard shipment={mockShipment} />)
    expect(screen.getByText('Av. Principal 123')).toBeInTheDocument()
    })

    it('muestra estado PENDIENTE correctamente', () => {
    const { container } = render(<ShipmentCard shipment={mockShipment} />)
    const statusBadge = container.querySelector('.shipment-status')
    expect(within(statusBadge).getByText(/Pendiente/i)).toBeInTheDocument()
    })

    it('muestra estado ENTREGADO correctamente', () => {
    const { container } = render(<ShipmentCard shipment={{ ...mockShipment, estado: 'ENTREGADO' }} />)
    const statusBadge = container.querySelector('.shipment-status')
    expect(within(statusBadge).getByText(/Entregado/i)).toBeInTheDocument()
    })

    it('muestra estado EN_CAMINO correctamente', () => {
    const { container } = render(<ShipmentCard shipment={{ ...mockShipment, estado: 'EN_CAMINO' }} />)
    const statusBadge = container.querySelector('.shipment-status')
    expect(within(statusBadge).getByText(/En tránsito/i)).toBeInTheDocument()
    })

    it('muestra el cliente', () => {
    render(<ShipmentCard shipment={mockShipment} />)
    expect(screen.getByText('Juan Perez')).toBeInTheDocument()
    })

    it('muestra el producto y cantidad', () => {
    render(<ShipmentCard shipment={mockShipment} />)
    expect(screen.getByText(/Laptop Dell/)).toBeInTheDocument()
    })

    it('muestra el total formateado', () => {
    render(<ShipmentCard shipment={mockShipment} />)
    expect(screen.getByText('$1999.98')).toBeInTheDocument()
    })

    it('muestra mensaje de error cuando shipment es null', () => {
    render(<ShipmentCard shipment={null} />)
    expect(screen.getByText('Envío no disponible')).toBeInTheDocument()
    })

    it('muestra No especificada cuando no hay direccion', () => {
    render(<ShipmentCard shipment={{ ...mockShipment, direccion: undefined }} />)
    expect(screen.getByText('No especificada')).toBeInTheDocument()
    })

    it('muestra el transportista cuando existe', () => {
    render(<ShipmentCard shipment={{ ...mockShipment, transportista: 'Chilexpress' }} />)
    expect(screen.getByText('Chilexpress')).toBeInTheDocument()
    })

    it('no muestra fila de transportista cuando no existe', () => {
    render(<ShipmentCard shipment={mockShipment} />)
    expect(screen.queryByText(/Transportista/)).not.toBeInTheDocument()
    })

    it('muestra la ruta estimada cuando existe', () => {
    render(<ShipmentCard shipment={{ ...mockShipment, rutaEstimada: 'Santiago - Valparaíso' }} />)
    expect(screen.getByText('Santiago - Valparaíso')).toBeInTheDocument()
    })

    it('usa valores por defecto cuando el estado no esta en el mapa', () => {
    const { container } = render(<ShipmentCard shipment={{ ...mockShipment, estado: 'ESTADO_RARO' }} />)
    const statusBadge = container.querySelector('.shipment-status')
    expect(within(statusBadge).getByText(/ESTADO_RARO/)).toBeInTheDocument()
    expect(statusBadge).toHaveStyle({ backgroundColor: '#999' })
    })

    it('muestra Desconocido cuando no hay estado', () => {
    const { container } = render(<ShipmentCard shipment={{ ...mockShipment, estado: undefined }} />)
    const statusBadge = container.querySelector('.shipment-status')
    expect(within(statusBadge).getByText(/Desconocido/)).toBeInTheDocument()
    })

    it('muestra el producto sin cantidad cuando esta no existe', () => {
    render(<ShipmentCard shipment={{ ...mockShipment, cantidad: undefined }} />)
    expect(screen.getByText('Laptop Dell')).toBeInTheDocument()
    })

    it('muestra la fecha formateada cuando existe', () => {
    render(<ShipmentCard shipment={{ ...mockShipment, fecha: '2024-05-10' }} />)
    expect(screen.getByText(/2024|10\/5|5\/10/)).toBeInTheDocument()
    })
})