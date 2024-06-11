package com.example.iotDataGenerator.controller;
import com.example.iotDataGenerator.entities.Attribute;
import com.example.iotDataGenerator.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/attributes")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @GetMapping
    public List<Attribute> getAllAttributes() {
        return attributeService.getAllAttributes();
    }

    @GetMapping("/{attributeId}")
    public ResponseEntity<Attribute> getAttributeById(@PathVariable Long attributeId) {
        Optional<Attribute> attribute = attributeService.getAttributeById(attributeId);
        return attribute.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{deviceId}")
    public ResponseEntity<Attribute> createAttribute(@RequestBody Attribute attribute,
                                                     @PathVariable Long deviceId) {
        Attribute createdAttribute = attributeService.createAttribute(attribute, deviceId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttribute);
    }

    @DeleteMapping("/{attributeId}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long attributeId) {
        attributeService.deleteAttribute(attributeId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("get_attributes_by-deviceId/{deviceId}")
    public List<Attribute> getAttributesByDeviceId(@PathVariable Long deviceId) {
        return attributeService.getAttributesByDeviceId(deviceId);
    }

    @PutMapping("/update/all/{deviceId}")
    public ResponseEntity<List<Attribute>> updateAttributesByDeviceId(@PathVariable Long deviceId, @RequestBody List<Attribute> updatedAttributes) {
        try {
            List<Attribute> updated = attributeService.updateAttributesByDeviceId(deviceId, updatedAttributes);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}

