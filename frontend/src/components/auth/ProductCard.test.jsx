import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import ProductCard from '../../../src/components/Product/ProductCard'

describe('ProductCard', () => {
    const mockProduct = {
    id: 1,
    nombre: 'Laptop Dell XPS',
    sku: 'SKU-0001',
    precio: 999.99,
    stock: 10,
    descripcion: 'Laptop de alta gama',
    bodegaNombre: 'Bodega Central Santiago',
    }

    it('renderiza el nombre del producto', () => {
    render(<ProductCard product={mockProduct} />)
    expect(screen.getByText('Laptop Dell XPS')).toBeInTheDocument()
    })

    it('renderiza el precio correctamente formateado', () => {
    render(<ProductCard product={mockProduct} />)
    expect(screen.getByText('$999.99')).toBeInTheDocument()
    })

    it('renderiza el stock del producto', () => {
    render(<ProductCard product={mockProduct} />)
    expect(screen.getByText('10 unidades')).toBeInTheDocument()
    })

    it('renderiza la descripcion si existe', () => {
    render(<ProductCard product={mockProduct} />)
    expect(screen.getByText('Laptop de alta gama')).toBeInTheDocument()
    })

    it('muestra badge Agotado cuando stock es 0', () => {
    render(<ProductCard product={{ ...mockProduct, stock: 0 }} />)
    expect(screen.getByText('Agotado')).toBeInTheDocument()
    })

    it('no muestra badge Agotado cuando hay stock', () => {
    render(<ProductCard product={mockProduct} />)
    expect(screen.queryByText('Agotado')).not.toBeInTheDocument()
    })

    it('muestra mensaje de error cuando product es null', () => {
    render(<ProductCard product={null} />)
    expect(screen.getByText('Producto no disponible')).toBeInTheDocument()
    })

    it('muestra Sin nombre cuando nombre es undefined', () => {
    render(<ProductCard product={{ ...mockProduct, nombre: undefined }} />)
    expect(screen.getByText('Sin nombre')).toBeInTheDocument()
    })

    it('renderiza el ID del producto', () => {
    render(<ProductCard product={mockProduct} />)
    expect(screen.getByText('ID: 1')).toBeInTheDocument()
    })

    it('renderiza el SKU del producto', () => {
    render(<ProductCard product={mockProduct} />)
    expect(screen.getByText('SKU: SKU-0001')).toBeInTheDocument()
    })

    it('renderiza la bodega/tienda de origen', () => {
    render(<ProductCard product={mockProduct} />)
    expect(screen.getByText('Bodega Central Santiago')).toBeInTheDocument()
    })

    it('muestra Sin asignar cuando no hay bodega', () => {
    render(<ProductCard product={{ ...mockProduct, bodegaNombre: undefined }} />)
    expect(screen.getByText('Sin asignar')).toBeInTheDocument()
    })

    it('aplica clase out-of-stock cuando stock es 0', () => {
    const { container } = render(<ProductCard product={{ ...mockProduct, stock: 0 }} />)
    expect(container.firstChild).toHaveClass('out-of-stock')
    })
})