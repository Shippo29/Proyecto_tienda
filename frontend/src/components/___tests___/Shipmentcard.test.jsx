import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
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
    render(<ShipmentCard shipment={mockShipment} />)
    expect(screen.getByText(/Pendiente/i)).toBeInTheDocument()
    })

    it('muestra estado ENTREGADO correctamente', () => {
    render(<ShipmentCard shipment={{ ...mockShipment, estado: 'ENTREGADO' }} />)
    expect(screen.getByText(/Entregado/i)).toBeInTheDocument()
    })

    it('muestra estado EN_CAMINO correctamente', () => {
    render(<ShipmentCard shipment={{ ...mockShipment, estado: 'EN_CAMINO' }} />)
    expect(screen.getByText(/En tránsito/i)).toBeInTheDocument()
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
})