package cz.cvut.fit.tjv.easyvps.controller.converter;


import cz.cvut.fit.tjv.easyvps.controller.dto.InstanceDTO;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import org.springframework.stereotype.Component;

@Component
public class InstanceDTOConverter implements DTOConverterInterface<InstanceDTO, Instance> {

    @Override
    public InstanceDTO toDTO(Instance instance) {
        return new InstanceDTO(instance.getId().getConfigurationId(),
                instance.getId().getUserId(), instance.getServer().getId(),
                instance.getIp(), instance.getId().getIpHash());
    }

    @Override
    public Instance toEntity(InstanceDTO instanceDTO) {
        return null;
    }
}
